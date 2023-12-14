package com.zvz09.xiaochen.blog.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zvz09.xiaochen.blog.domain.dto.ArticleDTO;
import com.zvz09.xiaochen.blog.domain.entity.Article;
import com.zvz09.xiaochen.blog.domain.entity.ArticleTag;
import com.zvz09.xiaochen.blog.domain.entity.Category;
import com.zvz09.xiaochen.blog.domain.entity.ReptileDocument;
import com.zvz09.xiaochen.blog.domain.entity.Tags;
import com.zvz09.xiaochen.blog.mapper.ArticleMapper;
import com.zvz09.xiaochen.blog.mapper.CategoryMapper;
import com.zvz09.xiaochen.blog.mapper.TagsMapper;
import com.zvz09.xiaochen.blog.service.IArticleService;
import com.zvz09.xiaochen.blog.service.IArticleTagService;
import com.zvz09.xiaochen.blog.service.IReptileDocumentService;
import com.zvz09.xiaochen.blog.strategy.ReptileService;
import com.zvz09.xiaochen.common.core.exception.BusinessException;
import com.zvz09.xiaochen.common.core.page.BasePage;
import com.zvz09.xiaochen.common.web.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author zvz09
 * @since 2023-12-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    private static final String IMG_URL_API = "https://api.btstu.cn/sjbz/api.php?lx=fengjing&format=json";

    private final CategoryMapper categoryMapper;
    private final TagsMapper tagsMapper;
    private final IArticleTagService articleTagService;
    private final ReptileService reptileService;
    private final IReptileDocumentService reptileDocumentService;


    @Value("${baidu.url:http://data.zz.baidu.com/urls?site=www.shiyit.com&token=aw5iVpNEB9aQJOYZ}")
    private String baiduUrl;

    @Override
    public IPage<Article> selectArticleList(BasePage basePage) {
        return this.page(new Page<>(basePage.getPageNum(), basePage.getPageSize()),
                new LambdaQueryWrapper<Article>()
                        .like(StringUtils.isNotBlank(basePage.getKeyword()), Article::getTitle, basePage.getKeyword())
                        .select(Article::getId, Article::getTitle, Article::getAvatar,
                                Article::getIsStick, Article::getIsPublish,
                                Article::getIsOriginal, Article::getOriginalUrl, Article::getQuantity,
                                Article::getIsRecommend
                        ));
    }

    @Override
    public Article selectArticleById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertArticle(ArticleDTO articleDTO) {
        Article blogArticle = ArticleDTO.convertToArticle(articleDTO);
        if (blogArticle == null) {
            throw new BusinessException("文章不存在!");
        }
        blogArticle.setUserId(String.valueOf(SecurityContextHolder.getUserId()));

        //添加分类
        Long categoryId = savaCategory(articleDTO.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(articleDTO);

        blogArticle.setCategoryId(categoryId);

        int insert = baseMapper.insert(blogArticle);
        if (insert > 0 && !tagList.isEmpty()) {
            List<ArticleTag> articleTags = new ArrayList<>();
            tagList.forEach(tag -> {
                articleTags.add(new ArticleTag(blogArticle.getId(), tag));
            });
            articleTagService.saveBatch(articleTags);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleDTO articleDTO) {
        Article blogArticle = baseMapper.selectById(articleDTO.getId());
        if (ObjectUtil.isNull(blogArticle)) {
            throw new BusinessException("文章不存在!");
        }
        //添加分类
        Long categoryId = savaCategory(articleDTO.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(articleDTO);

        blogArticle = ArticleDTO.convertToArticle(articleDTO);
        blogArticle.setCategoryId(categoryId);
        baseMapper.updateById(blogArticle);

        //先删出所有标签
        articleTagService.deleteByArticleIds(Collections.singletonList(blogArticle.getId()));
        //然后新增标签
        if (!tagList.isEmpty()) {
            List<ArticleTag> articleTags = new ArrayList<>();
            Long articleId = blogArticle.getId();
            tagList.forEach(tag -> {
                articleTags.add(new ArticleTag(articleId, tag));
            });
            articleTagService.saveBatch(articleTags);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatchArticle(List<Long> ids) {
        this.removeBatchByIds(ids);
        //先删出所有标签关联关系
        articleTagService.deleteByArticleIds(ids);
    }

    @Override
    public void topArticle(Long id) {
        Article article = this.getById(id);
        if (article != null) {
            article.setIsStick(!article.getIsStick());
            this.updateById(article);
        }
    }

    @Override
    public void psArticle(Long id) {
        Article article = this.getById(id);
        if (article != null) {
            article.setIsPublish(!article.getIsPublish());
            this.updateById(article);
        }
    }

    @Override
    public void seoBatch(List<Long> ids) {
       /* HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "data.zz.baidu.com");
        headers.add("User-Agent", "curl/7.12.1");
        headers.add("Content-Length", "83");
        headers.add("Content-Type", "text/plain");

        ids.forEach(item -> {
            String url = "http://www.shiyit.com/article/" + item;
            HttpEntity<String> entity = new HttpEntity<>(url, headers);
            restTemplate.postForObject(baiduUrl, entity, String.class);
        });*/
    }

    @Override
    @Transactional
    public void reptile(String url) {
        try {
            Document document = null;
            ReptileDocument reptileDocument = reptileDocumentService.getByUrl(url);
            if(reptileDocument!= null && StringUtils.isNotBlank(reptileDocument.getContent())){
                document = Jsoup.parse(reptileDocument.getContent());
            }else {
                document = Jsoup.connect(url).get();
                reptileDocument = reptileDocumentService.add(url,document);
            }
            ArticleDTO articleDTO = reptileService.execute(url,document);
            if(reptileDocument!= null && articleDTO !=null){
                reptileDocument.setStatus(true);
                reptileDocumentService.updateById(reptileDocument);
                if(this.count(new LambdaQueryWrapper<Article>().eq(Article::getOriginalUrl,url)) <= 0){
                    articleDTO.setIsOriginal(true);
                    articleDTO.setOriginalUrl(url);
                    articleDTO.setIsPublish(false);
                    this.insertArticle(articleDTO);
                }
            }
        } catch (IOException e) {
            log.error("爬取页面异常", e);
            throw new BusinessException("爬取页面异常");
        }
    }

    @Override
    public String randomImg() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(IMG_URL_API, String.class);
        return JSON.parseObject(result).get("imgurl").toString();
    }

    /**
     * 将数据库不存在的标签新增
     *
     * @param article
     * @return
     */
    private List<Long> getTagsList(ArticleDTO article) {
        List<Long> tagList = new ArrayList<>();
        article.getTags().forEach(item -> {
            Tags tags = tagsMapper.selectOne(new LambdaQueryWrapper<Tags>().eq(Tags::getName, item));
            if (tags == null) {
                tags = Tags.builder().name(item).sort(0).build();
                tagsMapper.insert(tags);
            }
            tagList.add(tags.getId());
        });
        return tagList;
    }

    /**
     * 如果分类不存在则新增
     *
     * @param categoryName
     * @return
     */
    private Long savaCategory(String categoryName) {
        if (StringUtils.isEmpty(categoryName)) {
            return 0L;
        }
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, categoryName));
        if (category == null) {
            category = Category.builder().name(categoryName).sort(0).build();
            categoryMapper.insert(category);
        }
        return category.getId();
    }
}
