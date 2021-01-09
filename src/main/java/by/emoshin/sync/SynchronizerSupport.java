package by.emoshin.sync;

public interface SynchronizerSupport {

    Synchronizer getSynchronizer();

    void setSynchronizer(Synchronizer synchronizer);

    void lock(LockMode lockMode);

    void unlock(LockMode lockMode);
}
