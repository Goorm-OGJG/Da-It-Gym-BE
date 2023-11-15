package com.ogjg.daitgym.feed.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FeedDetailResponse {

    private Long feedId;
    private String writer;
    private String writerImg;
    private LocalDateTime createdAt;
    private boolean liked;
    private boolean scrapped;
    private int likeCounts;
    private int scrapCounts;
    private List<FeedImageDto> imageLists;

    @QueryProjection
    public FeedDetailResponse(
            Long feedId, String writer,
            String writerImg, LocalDateTime createdAt
    ) {
        this.feedId = feedId;
        this.writer = writer;
        this.writerImg = writerImg;
        this.createdAt = createdAt;
    }

    public void setFeedDetails(
            boolean liked, boolean scrapped, int likeCounts,
            int scrapCounts, List<FeedImageDto> imageLists
    ) {
        this.liked = liked;
        this.scrapped = scrapped;
        this.likeCounts = likeCounts;
        this.scrapCounts = scrapCounts;
        this.imageLists = imageLists;
    }
}
