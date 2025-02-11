package me.shinsunyoung.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.domain.Article;
import me.shinsunyoung.springbootdeveloper.dto.ArticleListViewResponse;
import me.shinsunyoung.springbootdeveloper.dto.ArticleViewResponse;
import me.shinsunyoung.springbootdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// 블로그 글 전체 리스트를 담은 뷰를 반환합니다.
@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles); // 블로그 글 리스트 저장

        return "articleList"; // articleList.html라는 뷰 조회
    }

    // 블로그 글을 반환할 컨트롤러의 메서드 작성
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable("id") Long id, Model model){
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    // 수정 화면을 보여주기 위한 컨트롤러 메서드를 추가
    @GetMapping("/new-article")
    // id 키를 가진 쿼리 파라미터의 값을 id 변수에 매핑(id는 없을 수도 있음)
    public String newArticle(@RequestParam(name="id", required = false)Long id, Model model){
        if (id == null){ // 1. id가 없으면 생성
            model.addAttribute("article", new ArticleViewResponse());
        } else { // 2. id가 있으면 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
