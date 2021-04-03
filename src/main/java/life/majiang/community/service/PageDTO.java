package life.majiang.community.service;

import life.majiang.community.deo.QuestionDTO;

import java.util.ArrayList;
import java.util.List;

public class PageDTO {


    private List<QuestionDTO> questionDTOS;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private Integer pagel;
    private Integer pager;
    private List<Integer> pages = new ArrayList();
    private Integer totalPage;

    public void pages(Integer page, Integer count){
        int s = count.intValue();
        this.totalPage = count;
        this.page = page;
        this.pagel = page - 1;
        this.pager = page + 1;
        if (page <= 1){
            this.page = 1;
        }
        if (page>=count){
            this.page=count;
        }
        if (page == 1&&count!=1){
            showPrevious = showFirstPage = false;
            showEndPage = showNext = true;
        }
        if (page == count&&count!=1){
            showEndPage = showNext = false;
            showFirstPage = showPrevious = true;
        }
        if (page!=count&&page!=1){
            showFirstPage=showPrevious=showEndPage=showNext=true;
        }
        if (page>4){
            for (int i = page-3; i <= page; i++) {
                pages.add(i);
            }
        }else {
            for (int i = 1; i <= page; i++){
                pages.add(i);
            }
        }
        if (s-page>=4) {
            for (int i = page; i <= page+3; i++) {
                pages.add(i);
            }
        } else{
            for (int i = page; i <= s; i++) {
                pages.add(i);
            }
        }
        pages.remove(page);
    }
    public List<QuestionDTO> getQuestionDTOS() {
        return questionDTOS;
    }

    public void setQuestionDTOS(List<QuestionDTO> questionDTOS) {
        this.questionDTOS = questionDTOS;
    }

    public boolean isShowPrevious() {
        return showPrevious;
    }

    public void setShowPrevious(boolean showPrevious) {
        this.showPrevious = showPrevious;
    }

    public boolean isShowFirstPage() {
        return showFirstPage;
    }

    public void setShowFirstPage(boolean showFirstPage) {
        this.showFirstPage = showFirstPage;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public boolean isShowEndPage() {
        return showEndPage;
    }

    public void setShowEndPage(boolean showEndPage) {
        this.showEndPage = showEndPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPagel() {
        return pagel;
    }

    public void setPagel(Integer pagel) {
        this.pagel = pagel;
    }

    public Integer getPager() {
        return pager;
    }

    public void setPager(Integer pager) {
        this.pager = pager;
    }
}
