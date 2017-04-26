package filters;

import play.filters.cors.CORSFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class Filters extends DefaultHttpFilters {
    @Inject public Filters(CORSFilter corsFilter, LoggingFilter loggingFilter) {
        //super(corsFilter, loggingFilter);
        super(loggingFilter);
    }
}
