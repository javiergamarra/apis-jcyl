package com.api.demo;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class Page<E> {

    long PAGE_SIZE = 2L;

    long limit;
    @NotNull
    long page;

    public Page(List<E> list, long limit, long page) {
        this.results = list.stream().skip(PAGE_SIZE * page).limit(limit).toList();
        this.limit = limit;
        this.page = page;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public List<E> getResults() {
        return results;
    }

    public void setResults(List<E> results) {
        this.results = results;
    }

    List<E> results;
}
