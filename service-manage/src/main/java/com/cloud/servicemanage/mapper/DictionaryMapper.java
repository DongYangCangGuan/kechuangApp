package com.cloud.servicemanage.mapper;

import com.cloud.servicemanage.common.PageVo;
import com.cloud.commonsmng.entity.appletEntity.Dictionary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictionaryMapper {
    //查询总页数
    int getPageTotal(PageVo pageVo);

    List<Dictionary> getDictionaryInfo(PageVo pageVo);

    List<Dictionary> getKindnameInfo();

    int addDictionary(Dictionary dictionary);

    int addclassDictionary(Dictionary dictionary);

    int updateDictionary(Dictionary dictionary);

    int deleteDictionary(String id);

    int selectName(String name);

    int selectCode(String code);

    int selectName1(String name);

    int selectCode1(String code);

    int selectId(String id);

    List<Dictionary> selectByKind(@Param("kind") String kind);  //根据kind查询字典name


    //根据id查询字典信息
    Dictionary getDictionaryById(@Param("id") String id);

    List<Dictionary> selectByParent(@Param("parentId") String parentId,@Param("kind") String kind);
}
