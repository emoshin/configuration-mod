package by.emoshin.sync;

public enum NoOpSynchronizer implements Synchronizer {

    INSTANCE;

    @Override
    public void beginRead() {

    }

    @Override
    public void endRead() {

    }

    @Override
    public void beginWrite() {

    }

    @Override
    public void endWrite() {

    }
}
