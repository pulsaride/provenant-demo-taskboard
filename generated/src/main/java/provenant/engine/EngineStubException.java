package provenant.engine;

/**
 * Thrown by every body the engine could not carry from the legacy code (a
 * counted loss, marked TODO( in the source). It extends the exception the
 * stubs threw before (UnsupportedOperationException), so a catch clause or a
 * noRollbackFor of the legacy code behaves exactly as it did. The project's
 * EngineStubHandler answers it with HTTP 501.
 */
public class EngineStubException extends UnsupportedOperationException {

    private static final long serialVersionUID = 1L;

    public EngineStubException(String message) {
        super(message);
    }
}
