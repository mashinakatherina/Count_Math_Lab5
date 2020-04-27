public class NamingDoubleBiFunction {

    private DoubleBiFunction function;
    private String alias;
    private int count;

    public NamingDoubleBiFunction(DoubleBiFunction function, String alias, int count) {
        this.function = function;
        this.alias = alias;
        this.count = count;
    }

    public String getAlias() {
        return alias;
    }

    public int getFunctionNumber() {
        return count;
    }
}
