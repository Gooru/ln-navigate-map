package org.gooru.navigatemap.processor.coursepath.repositories.global;

/**
 * @author ashish on 3/3/17.
 */
public final class UsageRepositoryBuilder {

    private UsageRepositoryBuilder() {
        throw new AssertionError();
    }

    public static UsageFilterRepository buildUsageFilterService() {
        return new UsageFilterRepositoryImpl();
    }
}
