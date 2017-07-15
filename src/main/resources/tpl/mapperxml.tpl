<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${dataClassFullName}">
	<resultMap id="ResultMap" type="${modelClassFullName}">
		${ResultMap}
	</resultMap>
	<sql id="Columns">
		${cols}
	</sql>
	<sql id="TableName">
		${tableName}
	</sql>
	<!-- 增加 -->
	<insert id="save" ${useGeneratedKeys}>
		insert into <include refid="TableName" />
		${insertSqlFragment}
	</insert>
	<!-- 修改 -->
	<update id="update">
		update <include refid="TableName" />
		${updateSqlFragment}
		where ${objectKeyWhere}
	</update>
	<!-- 删除 -->
	<delete id="delete">
		<if test="object != null">
			delete from <include refid="TableName" />
			where ${objectKeyWhere}
		</if>
	</delete>
	<delete id="deleteAll">
		delete from <include refid="TableName" />
	</delete>
	<delete id="deleteByKey">
		<if test="key != null">
			delete from <include refid="TableName" /> where ${keyWhere}
		</if>
	</delete>
	<!-- 查询 -->
	<select id="getByKey" resultMap="ResultMap">
		<if test="key != null">
			select <include refid="Columns" /> from <include refid="TableName" />
			where ${keyWhere}
		</if>
	</select>
	<select id="findByExample" resultMap="ResultMap">
		select
		<include refid="Columns" />
		from
		<include refid="TableName" />
		<include refid="Common_Where_Fragment" />
		${orderByKeyDesc}
	</select>
	<select id="findOnePageByExample" resultMap="ResultMap">
		select
		<include refid="Columns" />
		from
		<include refid="TableName" />
		<include refid="Common_Where_Fragment" />
		<include refid="Order_By" />
	</select>
	<select id="getCountByExample" resultType="int">
		select count(*) from
		<include refid="TableName" />
		<include refid="Common_Where_Fragment" />
	</select>
	<select id="findOneByExample" resultMap="ResultMap">
		select
		<include refid="Columns" />
		from
		<include refid="TableName" />
		<include refid="Common_Where_Fragment" />
		limit 0,1
	</select>
	<select id="loadAll" resultMap="ResultMap">
		select
		<include refid="Columns" />
		from
		<include refid="TableName" />
		${orderByKeyDesc}
	</select>
	<select id="loadOnePage" resultMap="ResultMap">
		select
		<include refid="Columns" />
		from
		<include refid="TableName" />
		<include refid="Order_By" />
	</select>
	<select id="getCount" resultType="int">
		select count(*) from
		<include refid="TableName" />
	</select>
	<select id="search" resultMap="ResultMap" parameterType="map">
		select
		<include refid="Columns" />
		from
		<include refid="TableName" />
		<include refid="Search_Where_Fragment" />
		<include refid="Order_By" />
	</select>
	<select id="getCountForSearch" resultType="int">
		select count(*) from
		<include refid="TableName" />
		<include refid="Search_Where_Fragment" />
	</select>

	<sql id="Common_Where_Fragment">
		<trim prefix="where" suffixOverrides="and">
			<include refid="Common_Fragment" />
			${commonStringWhere}
		</trim>
	</sql>

	<sql id="Search_Where_Fragment">
		<trim prefix="where" suffixOverrides="and">
			<include refid="Common_Fragment" />
			${searchStringWhere}
		</trim>
	</sql>
	<sql id="Common_Fragment">
		${commonEqualWhere}
	</sql>

	<sql id="Order_By">
		<if test="page != null and page.orderBies != null">
			<foreach item="item" index="index" collection="page.orderBies" open="order by" separator="," close="">
                <choose>
                    ${orderByWhen}
                </choose>
			</foreach>
		</if>
	</sql>
</mapper>