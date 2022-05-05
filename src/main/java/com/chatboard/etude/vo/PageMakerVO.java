package com.chatboard.etude.vo;

import com.chatboard.etude.dto.post.PostSimpleDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(exclude="pageList")
public class PageMakerVO<T> {

    private Page<T> result;
    private Pageable prevPage;
    private Pageable nextPage;
    private int currentPageNum;
    private int totalPageNum;
    private Pageable currentPage;
    private List<Pageable> pageList;

    public PageMakerVO(Page<T> page) {
        this.result = page;
        this.currentPage = page.getPageable();
        this.currentPageNum = currentPage.getPageNumber() + 1;
        this.totalPageNum = page.getTotalPages();
        this.pageList = new ArrayList<>();
        calculatePages();
    }
    private void calculatePages() {
        int tempEndNum = (int)(Math.ceil(this.currentPageNum / 10.0) * 10);
        int startNum = tempEndNum - 9;
        Pageable startPage = this.currentPage;

        for (int i = startNum; i < this.currentPageNum; i++) {
            startPage = startPage.previousOrFirst();
        }
        this.prevPage = startPage.getPageNumber() <= 0 ? null : startPage.previousOrFirst();

        if (this.totalPageNum < tempEndNum) {
            tempEndNum = this.totalPageNum;
            this.nextPage = null;
        }

        for (int i = startNum; i <= tempEndNum; i++) {
            pageList.add(startPage);
            startPage = startPage.next();
        }
        this.nextPage = startPage.getPageNumber() + 1 < totalPageNum ? startPage : null;

    }
}
