package org.gooru.navigatemap.infra.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import org.gooru.navigatemap.infra.utilities.jdbi.PGArray;

/**
 * @author ashish on 15/3/17.
 */
public final class CollectionUtils {

    private CollectionUtils() {
        throw new AssertionError();
    }

    public static <T> List<T> intersect(List<T> input, List<T> intersector) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        List<T> result = new ArrayList<>(input.size());
        result.addAll(input);
        result.removeAll(intersector);
        return result;
    }

    public static <T> List<T> unique(List<T> input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        Set<T> resultSet = new HashSet<>(input);
        final ArrayList<T> result = new ArrayList<>(input.size());
        result.addAll(resultSet);
        return result;
    }

    public static <T> List<T> uniqueMaintainOrder(List<T> input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        Set<T> resultSet = new LinkedHashSet<>(input);
        final ArrayList<T> result = new ArrayList<>(input.size());
        result.addAll(resultSet);
        return result;
    }

    public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }

    public static <T, U> U[] convertArray(T[] from, Function<T, U> func, IntFunction<U[]> generator) {
        return Arrays.stream(from).map(func).toArray(generator);
    }

    public static PGArray<String> convertToSqlArrayOfString(List<String> input) {
        return PGArray.arrayOf(String.class, input);
    }

    public static PGArray<UUID> convertToSqlArrayOfUUID(List<String> input) {
        List<UUID> uuids = convertList(input, UUID::fromString);
        return PGArray.arrayOf(UUID.class, uuids);
    }

    public static PGArray<UUID> convertFromListUUIDToSqlArrayOfUUID(List<UUID> input) {
        return PGArray.arrayOf(UUID.class, input);
    }

}
