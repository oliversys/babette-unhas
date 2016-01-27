package br.com.oliversys.mobilecommons;

import java.util.List;
import java.util.Map;

public interface ISwappableExpandableView<T extends IValueObject> {
    void swapRecords(Map<String, List<T>> pizzasClassificadas);
}
