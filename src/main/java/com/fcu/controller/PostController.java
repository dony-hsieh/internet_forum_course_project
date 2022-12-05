package com.fcu.controller;

import com.fcu.model.PageHref;
import com.fcu.model.Post;
import com.fcu.model.User;
import com.fcu.security.SecurityService;
import com.fcu.service.PostService;
import com.fcu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {
    private static final int PAGE_ROWS = 10;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    // TODO: temp complete
    //  paging and paging href
    @GetMapping("/home")
    public String toHome(
            HttpServletRequest request,
            Model model,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page
    ) {
        String username = securityService.verifyCurrentUser(request.getSession());
        if (username == null) {
            return "redirect:login";
        }
        // construct paging
        Pageable paging = PageRequest.of(page, PAGE_ROWS);
        Page<Post> pagePost;
        if (q == null) {
            pagePost = postService.findAll(paging);
        } else {
            pagePost = postService.findByTitleContaining(q, paging);
        }
        List<Post> postList = pagePost.getContent();
        // construct paging href
        List<PageHref> pageHrefList = new ArrayList<>();
        String hrefStr0 = "/home?page=";
        String hrefStr;
        if (page - 2 >= 0) {
            for (int i = page - 2; i < page; i++) {
                if (q != null) {
                    hrefStr = hrefStr0 + i + "&q=" + q;
                } else {
                    hrefStr = hrefStr0 + i;
                }
                pageHrefList.add(new PageHref(i, hrefStr));
            }
        }
        for (int i = page; i < page + 3; i++) {
            if (q != null) {
                hrefStr = hrefStr0 + i + "&q=" + q;
            } else {
                hrefStr = hrefStr0 + i;
            }
            pageHrefList.add(new PageHref(i, hrefStr));
        }
        // add model attribute
        model.addAttribute("post_list", postList);
        model.addAttribute("page_href", pageHrefList);
        return "home";
    }

    @GetMapping("/home/postnew")
    public String toPostNew(HttpServletRequest request) {
        String username = securityService.verifyCurrentUser(request.getSession());
        if (username == null) {
            return "redirect:/login";
        }
        return "post_new";
    }

    @PostMapping("/home/postnew")
    public String handlePostNewQuestion(
            HttpServletRequest request,
            @RequestParam Map<String, String> postContents
    ) {
        String username = securityService.verifyCurrentUser(request.getSession());
        if (username == null) {
            return "redirect:/login";
        }
        if (postContents == null) {
            return "redirect:/home";
        }
        User user = userService.findOneById(username);
        Post post = new Post();
        Date curDate = new Date();
        if (user == null) {
            return "redirect:/login";
        }
        post.setTitle(postContents.get("title"));
        post.setContent(postContents.get("content"));
        post.setVotingUp(0);
        post.setVotingDown(0);
        post.setPostDate(curDate);
        post.setModifyDate(curDate);
        post.setUser(user);
        postService.insertOne(post);
        return "redirect:/home";
    }

    @GetMapping("/home/post")
    public String toPostById(
            HttpServletRequest request,
            Model model,
            @RequestParam("id") int id
    ) {
        String username = securityService.verifyCurrentUser(request.getSession());
        if (username == null) {
            return "redirect:/login";
        }
        // get post by id
        Post post = postService.findOneById(id);
        if (post == null) {
            return "redirect:/home";
        }
        model.addAttribute("post", post);
        return "show_post";
    }
}
