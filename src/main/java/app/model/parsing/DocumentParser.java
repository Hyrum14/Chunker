package app.model.parsing;

public class DocumentParser
{
    StringBuilder currentWord = new StringBuilder();
    StringBuilder currentDelimiter = new StringBuilder();
    StringBuilder contextLetters = new StringBuilder();
    DocumentProvider input;
    DocumentStorage documentStorage;

    private boolean end()
    {
        return input.end();
    }

    private int peek()
    {
        return input.peek();
    }

    private void advance() throws DocumentParsingException
    {
        try
        {
            input.advance();
        } catch (IndexOutOfBoundsException ex)
        {
            throw new DocumentParsingException(ex.getMessage());
        }
    }

    private void error() throws DocumentParsingException
    {
        throw new DocumentParsingException("Invalid character: " + peek());
    }

    public void parse(DocumentProvider input, DocumentStorage documentStorage) throws DocumentParsingException
    {
        this.input = input;
        this.documentStorage = documentStorage;
        currentWord.setLength(0);
        currentDelimiter.setLength(0);

        State state = State.BEGIN;

        while (state != State.END)
        {
            switch (state)
            {
                case BEGIN:
                    if (end())
                    {
                        state = State.END;
                    } else if (CharacterCategorizer.isSplit(peek()) || CharacterCategorizer.isContextLetter(peek()))
                    {
                        state = State.DELIMITER;
                    } else if (CharacterCategorizer.isInvalid(peek()))
                    {
                        handleInvalidCharacterFound();
                        state = State.INVALIDATED;
                    } else if (CharacterCategorizer.isLetter(peek()))
                    {
                        state = State.LETTER;
                    } else
                    {
                        error();
                    }
                    break;

                case LETTER:
                    if (end())
                    {
                        state = State.END;
                    } else if (CharacterCategorizer.isSplit(peek()))
                    {
                        handleWordFinished();
                        state = State.DELIMITER;
                    } else if (CharacterCategorizer.isInvalid(peek()))
                    {
                        handleInvalidCharacterFound();
                        state = State.INVALIDATED;
                    } else if (CharacterCategorizer.isLetter(peek()))
                    {
                        currentWord.append(Character.toChars(peek()));
//                        state = State.LETTER;
                        advance();
                    } else if (CharacterCategorizer.isContextLetter(peek()))
                    {
//                        currentWord.append(Character.toChars(peek()));
                        state = State.CONTEXT_CHARACTER;
//                        advance();
                    } else
                    {
                        error();
                    }
                    break;

                case DELIMITER:
                    if (end())
                    {
                        state = State.END;
                    } else if (CharacterCategorizer.isSplit(peek()) || CharacterCategorizer.isContextLetter(peek()))
                    {
                        currentDelimiter.append(Character.toChars(peek()));
                        advance();
                    } else if (CharacterCategorizer.isInvalid(peek()))
                    {
                        state = State.INVALIDATED;
                    } else if (CharacterCategorizer.isLetter(peek()))
                    {
                        state = State.LETTER;
                    } else
                    {
                        error();
                    }
                    break;

                case INVALIDATED:
                    if (end())
                    {
                        state = State.END;
                    } else if (CharacterCategorizer.isSplit(peek()))
                    {
                        state = State.DELIMITER;
                    } else if (CharacterCategorizer.isInvalid(peek()))
                    {
                        currentDelimiter.append(Character.toChars(peek()));
                        advance();
                    } else if (CharacterCategorizer.isLetter(peek()) || CharacterCategorizer.isContextLetter(peek()))
                    {
                        currentDelimiter.append(Character.toChars(peek()));
                        advance();
                    } else
                    {
                        error();
                    }
                    break;
                case CONTEXT_CHARACTER:
                    if (end())
                    {
                        state = State.END;
                    } else if (CharacterCategorizer.isSplit(peek()))
                    {
                        handleWordFinished();
                        state = State.DELIMITER;
                    } else if (CharacterCategorizer.isInvalid(peek()))
                    {
                        state = State.INVALIDATED;
                    } else if (CharacterCategorizer.isLetter(peek()))
                    {
                        currentWord.append(contextLetters.toString());
                        contextLetters.setLength(0);
                        state = State.LETTER;
                    } else if (CharacterCategorizer.isContextLetter(peek()))
                    {
                        contextLetters.append(Character.toChars(peek()));
                        advance();
                    } else
                    {
                        error();
                    }
                    break;
            }
        }
        if (!currentWord.isEmpty())
        {
            documentStorage.storeDelimiter(currentDelimiter.toString());
            currentDelimiter.setLength(0);
            documentStorage.storeOccurrence(currentWord.toString());
            currentWord.setLength(0);
        }
        currentDelimiter.append(contextLetters.toString());
        contextLetters.setLength(0);
        documentStorage.storeDelimiter(currentDelimiter.toString());
        currentDelimiter.setLength(0);
    }

    private void handleInvalidCharacterFound()
    {
        currentDelimiter.append(currentWord);
        currentWord.setLength(0);
    }

    private void handleWordFinished()
    {

        documentStorage.storeDelimiter(currentDelimiter.toString());
        currentDelimiter.setLength(0);

        documentStorage.storeOccurrence(currentWord.toString());
        currentWord.setLength(0);

        if (!contextLetters.isEmpty())
        {
            currentDelimiter.append(contextLetters);
            contextLetters.setLength(0);
        }
    }

    private enum State
    {BEGIN, LETTER, CONTEXT_CHARACTER, DELIMITER, INVALIDATED, END}

    public interface DocumentStorage
    {
        void storeDelimiter(String contents);

        void storeOccurrence(String contents);
    }

    public interface DocumentProvider
    {
        boolean end();

        int peek();

        void advance() throws IndexOutOfBoundsException;
    }
}
