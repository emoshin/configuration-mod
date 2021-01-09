package by.emoshin.sync;

public interface Synchronizer {

    void beginRead();

    void endRead();

    void beginWrite();

    void endWrite();
}
