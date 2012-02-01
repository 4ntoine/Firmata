package name.antonsmirnov.firmata.wrapper;

/**
 * At least one filter should allow data
 */
public class OrFilter implements IMessageFilter {

    private IMessageFilter[] filters;

    public OrFilter(IMessageFilter... filters) {
        this.filters = filters;
    }

    public boolean isAllowed(MessageWithProperties data) {
        for (IMessageFilter eachFilter : filters)
            if (eachFilter.isAllowed(data))
                return true;

        return false;
    }
}
