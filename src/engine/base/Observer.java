package engine.base;

public interface Observer
{
    public void onChange(Observable observable);
    public void onRegister(Observable observable);
    public void onUnregister(Observable observable);
}