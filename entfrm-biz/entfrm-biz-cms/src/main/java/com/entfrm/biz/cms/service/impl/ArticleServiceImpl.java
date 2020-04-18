package com.entfrm.biz.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.entfrm.biz.cms.mapper.ArticleMapper;
import com.entfrm.biz.cms.entity.Article;
import com.entfrm.biz.cms.service.ArticleService;

/**
 * @author entfrm
 * @date 2020-04-01 10:04:11
 * @description 文章Service业务层
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}
