package com.home.fileserver.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {

    private int pageNumber;
    private int totalPages;
    private long totalAmount;
    private String next;
    private String previous;
    private List<T> result;

    public PageResponse(Page<T> page, String basePath) {
        pageNumber = page.getNumber();
        result = page.getContent();
        totalAmount = page.getTotalElements();
        totalPages = page.getTotalPages();
        if ((pageNumber + 1) < totalPages) {
            next = basePath + "?pageNumber=" + (pageNumber + 1) + "&limit=" + page.getSize();
        }
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
