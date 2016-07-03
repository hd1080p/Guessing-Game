package cli;
import core.IClient;

class ClientCLI implements IClient {
    public void win()
    {
        System.out.println("You won!");
    }

    public void lose()
    {
        System.out.println("You are the worst!");
    }

    public void tooLow(int guess)
    {
        System.out.printf("%d is too low!\n", guess);
    }

    public void tooHigh(int guess)
    {
        System.out.printf("%d is too high!\n", guess);
    }
}
