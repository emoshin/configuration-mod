package by.emoshin.sync;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteSynchronizer implements Synchronizer {

    private final ReadWriteLock lock;

    public ReadWriteSynchronizer() {
        this(null);
    }

    public ReadWriteSynchronizer(final ReadWriteLock lock) {
        this.lock = lock == null ? createDefaultLock() : lock;
    }

    @Override
    public void beginRead() {
        lock.readLock().lock();
    }

    @Override
    public void endRead() {
        lock.readLock().unlock();
    }

    @Override
    public void beginWrite() {
        lock.writeLock().lock();
    }

    @Override
    public void endWrite() {
        lock.writeLock().unlock();
    }

    private static ReadWriteLock createDefaultLock() {
        return new ReentrantReadWriteLock();
    }
}
