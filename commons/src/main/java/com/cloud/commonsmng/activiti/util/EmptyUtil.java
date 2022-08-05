package com.cloud.commonsmng.activiti.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @ClassName: EmptyUtil
 * @Description: TODO(这里用一句话描述这个类的作用) 判断非空
 * @author chy
 * @date 2019年1月24日 上午11:02:06
 */
public class EmptyUtil {

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) list集合为空
	 * @param list
	 * @return: boolean
	 */
	public static boolean isEmptyList(List<?> list) {
		return (null == list || list.isEmpty());
	}

	/**
	 * @Title: EmptyUtil 若lists中有一個list為空 ，則返回true
	 * @param lists
	 * @return
	 */
	public static boolean isEmptyLists(List<?>... lists) {
		for (List list : lists) {
			if (isEmptyList(list)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) list集合不为空
	 * @param list
	 * @return: boolean
	 */
	public static boolean isNotEmptyList(List<?> list) {
		return !isEmptyList(list);
	}

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) map集合为空
	 * @param map
	 * @return: boolean
	 */
	public static boolean isEmptyMap(Map<?, ?> map) {
		return (null == map || map.isEmpty());
	}

	/**
	 * @Title: EmptyUtil 若maps中有一個map為空 ，則返回true
	 * @param maps
	 * @return
	 */
	public static boolean isEmptyMaps(Map<?, ?>... maps) {
		for (Map map : maps) {
			if (isEmptyMap(map)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) map集合不为空
	 * @param map
	 * @return: boolean
	 */
	public static boolean isNotEmptyMap(Map<?, ?> map) {
		return !isEmptyMap(map);
	}

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) set集合为空
	 * @param set
	 * @return: boolean
	 */
	public static boolean isEmptySet(Set<?> set) {
		return (null == set || set.isEmpty());
	}

	/**
	 * @Title: EmptyUtil 若maps中有一個map為空 ，則返回true
	 * @param maps
	 * @return
	 */
	public static boolean isEmptySets(Set<?>... sets) {
		for (Set set : sets) {
			if (isEmptySet(set)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @Title: EmptyUtil 若collections中有一個collection為空 ，則返回true
	 * @param collections
	 * @return
	 */
	public static boolean isEmptyCollection(Collection<?>... collections) {
		for (Collection col : collections) {
			if (col == null || col.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) set集合不为空
	 * @param set
	 * @return: boolean
	 */
	public static boolean isNotEmptySet(Set<?> set) {
		return !isEmptySet(set);
	}

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) object[]类型数组为空
	 * @param objs
	 * @return: boolean
	 */
	public static boolean isEmptyArray(Object[] objs) {
		return (null == objs || objs.length == 0);
	}

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) object[]类型数组不为空
	 * @param objs
	 * @return: boolean
	 */
	public static boolean isNotEmptyArray(Object[] objs) {
		return !isEmptyArray(objs);
	}

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) object对象为空
	 * @param obj
	 * @return: boolean
	 */
	public static boolean isEmptyObject(Object obj) {
		return (null == obj || "".equals(obj));
	}

	/**
	 * 
	 * @Title: EmptyUtil TODO: TODO(用一句话描述这个方法的作用) object对象不为空
	 * @param obj
	 * @return: boolean
	 */
	public static boolean isNotEmptyObject(Object obj) {
		return !isEmptyObject(obj);
	}

	/**
	 * @Title: EmptyUtil 若Objects中有一個object為空 ，則返回true
	 * @param objects
	 * @return
	 */
	public static boolean isEmptyObjects(Object... objects) {
		for (Object obj : objects) {
			if (isEmptyObject(obj)) {
				return true;
			}
		}
		return false;
	}

}
