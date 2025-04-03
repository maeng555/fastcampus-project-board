package org.fastcampus.projectboard.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString //쉽게 출력을 보기위해
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
}) // 테이블 서칭이 가능하게끔

@Entity
public class Article extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Column(nullable = false,length = 10000)
    @Setter
    private String content;

    @Setter
    private String hashtag;
    // 이렇게 세터를 따로 건이유 - id 나메타데이터는 내가 조정하는게아님  jpa 영속성할때 자동으로부여

    @ToString.Exclude //연결고리 끊기
    @OrderBy("id") //정렬기준
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();



    protected Article() {} //외부에서 new를 사용못하게 하기위해

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
       return new Article(title, content, hashtag);
    } // 생성자를 private으로하고 외부에서 New 키워드없이 생성 하기위해 static 사용

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

