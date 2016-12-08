package com.webratio.units.store.social;

import java.util.List;

public final class ResultFragment {
    private final boolean hasNext;
    private final List resultList;

    public ResultFragment(boolean hasNext, List resultList) {
        this.hasNext = hasNext;
        this.resultList = resultList;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public List getResultList() {
        return resultList;
    }

}
