package core;

public interface IClient {
    void win();
    void lose();
    void tooLow(int guess);
    void tooHigh(int guess);
}
