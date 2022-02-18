package uj.java.map2d;

import java.util.*;
import java.util.function.Function;

public class DefaultMap2D<R, C, V> implements Map2D<R, C, V> {
    final private Map<R, Map<C, V>> mapOfMaps;

    public DefaultMap2D() {
        mapOfMaps = new HashMap<>();
    }

    @Override
    public V put(R rowKey, C columnKey, V value) {
        if (rowKey == null || columnKey == null)
            throw new NullPointerException();

        if (!mapOfMaps.containsKey(rowKey))
            mapOfMaps.put(rowKey, new HashMap<>());

        return mapOfMaps.get(rowKey).put(columnKey, value);
    }

    @Override
    public V get(R rowKey, C columnKey) {
        return getOrDefault(rowKey, columnKey, null);
    }

    @Override
    public V getOrDefault(R rowKey, C columnKey, V defaultValue) {
        return mapOfMaps.containsKey(rowKey) ? mapOfMaps.get(rowKey).get(columnKey) : defaultValue;
    }

    @Override
    public V remove(R rowKey, C columnKey) {
        return mapOfMaps.containsKey(rowKey) ? mapOfMaps.get(rowKey).remove(columnKey) : null;
    }

    @Override
    public boolean isEmpty() {
        return mapOfMaps.isEmpty();
    }

    @Override
    public boolean nonEmpty() {
        return !isEmpty();
    }

    @Override
    public int size() {
        final int[] size = {0};
        mapOfMaps.forEach((k, v) -> size[0] += v.size());
        return size[0];
    }

    @Override
    public void clear() {
        mapOfMaps.clear();
    }

    private Map<C, V> copyRowAndReturnUnmodifiable(R rowKey) {
        var returnMap = new HashMap<>(mapOfMaps.get(rowKey));
        return Collections.unmodifiableMap(returnMap);
    }

    @Override
    public Map<C, V> rowView(R rowKey) {
        if (mapOfMaps.containsKey(rowKey))
            return this.copyRowAndReturnUnmodifiable(rowKey);

        return Collections.emptyMap();
    }

    @Override
    public Map<R, V> columnView(C columnKey) {
        Map<R, V> returnMap = new HashMap<>();
        mapOfMaps.forEach((k, v) -> {
            if (v.containsKey(columnKey))
                returnMap.put(k, v.get(columnKey));
        });

        return Collections.unmodifiableMap(returnMap);
    }

    @Override
    public boolean hasValue(V value) {
        for (var map : mapOfMaps.values())
            if (map.containsValue(value))
                return true;

        return false;
    }

    @Override
    public boolean hasKey(R rowKey, C columnKey) {
        return mapOfMaps.containsKey(rowKey) && mapOfMaps.get(rowKey).containsKey(columnKey);
    }

    @Override
    public boolean hasRow(R rowKey) {
        return mapOfMaps.containsKey(rowKey);
    }

    @Override
    public boolean hasColumn(C columnKey) {
        for (var map : mapOfMaps.values())
            if (map.containsKey(columnKey))
                return true;

        return false;
    }

    @Override
    public Map<R, Map<C, V>> rowMapView() {

        Map<R, Map<C, V>> returnMap = new HashMap<>();

        for (var key : mapOfMaps.keySet())
            returnMap.put(key, this.copyRowAndReturnUnmodifiable(key));

        return Collections.unmodifiableMap(returnMap);
    }

    @Override
    public Map<C, Map<R, V>> columnMapView() {

        Map<C, Map<R, V>> returnMap = new HashMap<>();

        for (var map : mapOfMaps.values())
            for (var key : map.keySet())
                if (!returnMap.containsKey(key))
                    returnMap.put(key, columnView(key));

        return Collections.unmodifiableMap(returnMap);
    }

    @Override
    public Map2D<R, C, V> fillMapFromRow(Map<? super C, ? super V> target, R rowKey) {
        if (mapOfMaps.containsKey(rowKey))
            target.putAll(mapOfMaps.get(rowKey));

        return this;
    }

    @Override
    public Map2D<R, C, V> fillMapFromColumn(Map<? super R, ? super V> target, C columnKey) {
        for (var entry : mapOfMaps.entrySet())
            if (entry.getValue().containsKey(columnKey))
                target.put(entry.getKey(), entry.getValue().get(columnKey));

        return this;
    }

    @Override
    public Map2D<R, C, V> putAll(Map2D<? extends R, ? extends C, ? extends V> source) {
        var rowMapView = source.rowMapView();

        for (var entry : rowMapView.entrySet()) {
            if (!mapOfMaps.containsKey(entry.getKey()))
                mapOfMaps.put(entry.getKey(), new HashMap<>());
            mapOfMaps.get(entry.getKey()).putAll(entry.getValue());
        }

        return this;
    }

    @Override
    public Map2D<R, C, V> putAllToRow(Map<? extends C, ? extends V> source, R rowKey) {
        if (!mapOfMaps.containsKey(rowKey))
            mapOfMaps.put(rowKey, new HashMap<>());

        mapOfMaps.get(rowKey).putAll(source);

        return this;
    }

    @Override
    public Map2D<R, C, V> putAllToColumn(Map<? extends R, ? extends V> source, C columnKey) {
        for (var entry : source.entrySet()) {
            if (!mapOfMaps.containsKey(entry.getKey()))
                mapOfMaps.put(entry.getKey(), new HashMap<>());

            mapOfMaps.get(entry.getKey()).put(columnKey, entry.getValue());
        }

        return this;
    }

    @Override
    public <R2, C2, V2> Map2D<R2, C2, V2> copyWithConversion(Function<? super R, ? extends R2> rowFunction, Function<? super C, ? extends C2> columnFunction, Function<? super V, ? extends V2> valueFunction) {
        Map2D<R2, C2, V2> returnMap2D = new DefaultMap2D<>();

        for (var entryOuter : mapOfMaps.entrySet())
            for (var entryInner : entryOuter.getValue().entrySet())
                returnMap2D.put(rowFunction.apply(entryOuter.getKey()), columnFunction.apply(entryInner.getKey()),
                        valueFunction.apply(entryInner.getValue()));

        return returnMap2D;
    }
}