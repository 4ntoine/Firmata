package name.antonsmirnov.firmata.wrapper;

/**
 *  All filters should allow data
 */
public class AndFilter implements IMessageFilter{

    private IMessageFilter[] filters;

    public AndFilter(IMessageFilter... filters) {
        this.filters = filters;
    }

    public boolean isAllowed(MessageWithProperties data) {
        for (IMessageFilter eachFilter : filters)
            if (!eachFilter.isAllowed(data))
                return false;

        return true;
    }
}
