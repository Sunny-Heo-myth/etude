package org.alan.etude.dto.post;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString(exclude="pageList")
public class PostPageDto<T> {

    private Page<T> result;
    private Pageable prevPage;
    private Pageable currentPage;
    private Pageable nextPage;
    private int currentPageNum;
    private int totalPageNum;
    private List<Pageable> pageList;
    private List<Long> categoryId;
    private List<Long> memberId;

    public PostPageDto(Page<T> page, List<Long> categoryId, List<Long> memberId) {
        this.result = page;
        this.currentPage = page.getPageable();
        this.currentPageNum = currentPage.getPageNumber() + 1;
        this.totalPageNum = page.getTotalPages();
        this.pageList = new ArrayList<>();
        this.categoryId = categoryId;
        this.memberId = memberId;
        calculatePages();
    }

    public String getCategoryId() {
        return this.categoryId.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public String getMemberId() {
        return this.memberId.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private void calculatePages() {
        int tempEndNum = (int)(Math.ceil(this.currentPageNum / 10.0) * 10);
        int tempStartNum = tempEndNum - 9;
        Pageable startPage = this.currentPage;

        for (int i = tempStartNum; i < this.currentPageNum; i++) {
            startPage = startPage.previousOrFirst();
        }
        this.prevPage = startPage.getPageNumber() <= 0 ? null : startPage.previousOrFirst();

        if (this.totalPageNum < tempEndNum) {
            tempEndNum = this.totalPageNum;
            this.nextPage = null;
        }

        for (int i = tempStartNum; i <= tempEndNum; i++) {
            pageList.add(startPage);
            startPage = startPage.next();
        }
        this.nextPage = startPage.getPageNumber() + 1 < totalPageNum ? startPage : null;

    }
}
