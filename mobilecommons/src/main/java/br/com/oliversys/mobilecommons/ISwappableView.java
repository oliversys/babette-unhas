package br.com.oliversys.mobilecommons;

import java.util.List;

/**
 * Created by William on 5/2/2015.
 */
public interface ISwappableView<T extends IValueObject> {
    void swapRecords(List<T> objects);
}
