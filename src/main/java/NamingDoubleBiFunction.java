public class NamingDoubleBiFunction {


    private DoubleBiFunction function;
    private String alias;

    public NamingDoubleBiFunction(DoubleBiFunction function, String alias) {
        this.function = function;
        this.alias = alias;
    }

    public DoubleBiFunction getFunction() {
        return function;
    }

    public String getAlias() {
        return alias;
    }

}
