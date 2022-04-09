package com.example.shop.repository.item;

import com.example.shop.Dtos.item.AdminItemFormDto;
import com.example.shop.Dtos.item.ItemSearchDto;
import com.example.shop.Dtos.item.UserItemFormDto;
import com.example.shop.domain.Item;
import com.example.shop.enumtype.ItemCategory;
import com.example.shop.enumtype.ItemStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.example.shop.domain.QItem.*;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory jpaQueryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AdminItemFormDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<Item> result = jpaQueryFactory.selectDistinct(item)
                .from(item)
                .join(item.itemImgList).fetchJoin()
                .where(item.status.ne(ItemStatus.DELETED),
                        searchItemStatusEq(itemSearchDto.getItemStatus()),
                        searchItemCategoryEq(itemSearchDto.getItemCategory()),
                        itemNameLike(itemSearchDto.getItemName()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(item.count())
                .from(item)
                .where(searchItemStatusEq(itemSearchDto.getItemStatus()),
                        searchItemCategoryEq(itemSearchDto.getItemCategory()),
                        itemNameLike(itemSearchDto.getItemName()));

        Page<Item> page = PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);

        return page.map(AdminItemFormDto::of);
    }

    @Override
    public Page<UserItemFormDto> getItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> result = jpaQueryFactory.selectDistinct(item)
                .from(item)
                .join(item.itemImgList).fetchJoin()
                .where(item.status.ne(ItemStatus.DELETED),
                        searchItemStatusEq(itemSearchDto.getItemStatus()),
                        searchItemCategoryEq(itemSearchDto.getItemCategory()),
                        itemNameLike(itemSearchDto.getItemName()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(item.count())
                .from(item)
                .where(searchItemStatusEq(itemSearchDto.getItemStatus()),
                        searchItemCategoryEq(itemSearchDto.getItemCategory()),
                        itemNameLike(itemSearchDto.getItemName()));

        Page<Item> page = PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);

        return page.map(UserItemFormDto::of);
    }

    private BooleanExpression searchItemCategoryEq(ItemCategory itemCategory) {
        return itemCategory == null ? null : item.category.eq(itemCategory);
    }

    private BooleanExpression itemNameLike(String itemName) {
        return itemName == null || itemName.isEmpty() ? null : item.itemName.like("%" + itemName + "%");
    }

    private BooleanExpression searchItemStatusEq(ItemStatus itemStatus) {
        return itemStatus == null ? null : item.status.eq(itemStatus);
    }
}
