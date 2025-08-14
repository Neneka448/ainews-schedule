package com.mortis.ainews.application.persistence.po.users;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ai_news_user_subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSubScriptionRel {

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static public class UserSubScriptionRelPK implements java.io.Serializable {
        @Column(name = "user_id")
        private Long userId;
        @Column(name = "keyword_id")
        private Long keywordId;
    }

    @EmbeddedId
    private UserSubScriptionRelPK id;

    private int deleted;
}
