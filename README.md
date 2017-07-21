# server-code-generator



给定一个数据库，自动生成后台代码(spring mvc/mybatis/jquery/bootstrap)，同时能够生成一个简单的后台对应的页面
<br/>
注：生成后的工程是用maven来管理；<strong>数据库中的表注释、列注释，都会体现到代码中，所以，在建表的时候，希望尽可能的有详尽的注释</strong>
<br/>

<strong>基本功能：</strong><br/>

&nbsp;&nbsp;&nbsp;&nbsp;1.生成数据库对应的pojo<br/>

&nbsp;&nbsp;&nbsp;&nbsp;2.生成mybatis的mapper xml<br/>

&nbsp;&nbsp;&nbsp;&nbsp;3.生成dao层代码(放置在data包下，因为mybatis官方取名为data，所以，没有写成dao/mapper包名)<br/>

&nbsp;&nbsp;&nbsp;&nbsp;4.生成service层代码<br/>

&nbsp;&nbsp;&nbsp;&nbsp;5.生成controller层代码<br/>

&nbsp;&nbsp;&nbsp;&nbsp;6.生成登录界面<br/>

&nbsp;&nbsp;&nbsp;&nbsp;7.生成各模块的页面代码（增删查改，对于一般的管理后台稍微修改一下）<br/>

&nbsp;&nbsp;&nbsp;&nbsp;8.数据库、spring配置文件自动生成<br/>

<strong>src/main/resources/config.xml文件说明：</strong><br/>

&nbsp;&nbsp;&nbsp;&nbsp;1.author：作者名，可以是你自己的名字，也可以是你公司的名字<br/>

&nbsp;&nbsp;&nbsp;&nbsp;2.dbDriver:数据库驱动，如果不是mysql，或者pom中的mysql版本不是你想要的，则需要修改pom.xml文件<br/>

&nbsp;&nbsp;&nbsp;&nbsp;3.dbUrl/dbUser/dbPswd:数据库的url、用户名、密码，用于连接数据库<br/>

&nbsp;&nbsp;&nbsp;&nbsp;4.projectPath：生成的代码的目标位置<br/>

&nbsp;&nbsp;&nbsp;&nbsp;5.rootPackage:所有java代码存放在此包下<br/>

&nbsp;&nbsp;&nbsp;&nbsp;6.tablePrefix:表前缀，一般是"tb_",比如：tb_this_table<br/>

&nbsp;&nbsp;&nbsp;&nbsp;7.tableNameSeparator:数据库表名的分隔符，一般是"_",例如：tb_this_table<br/>

&nbsp;&nbsp;&nbsp;&nbsp;8.colNameSeparator:列分隔符，一般是"_",比如：col_name<br/>

&nbsp;&nbsp;&nbsp;&nbsp;9.includeTable:需要处理的表，如果只处理部分表，则可以在这里设置，多个表用逗号隔开，优先级低于excludeTable<br/>

&nbsp;&nbsp;&nbsp;&nbsp;10.excludeTable:不处理的表，必须指定表名，不能是通配符"*"<br/>

&nbsp;&nbsp;&nbsp;&nbsp;11.table:有些表需要做一些特殊的命名，则可以自定义对应的类名，同时也可以指定生成的目标包名<br/>
	
注：列暂不能自定义名称


<strong>运行后台：</strong><br/>

&nbsp;&nbsp;&nbsp;&nbsp;1.eclipse：import->Existing Maven Projects；idea：file->open<br/>
&nbsp;&nbsp;&nbsp;&nbsp;2.等待maven下载依赖的jar包(开始会报错，maven下完jar包就好了)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;3.运行 maven clean install<br/>
&nbsp;&nbsp;&nbsp;&nbsp;4.将war包放入tomcat的webapps中,或者配置tomcat的conf/server.xml的&lt;Context doc="" path=""/&gt;<br/>
&nbsp;&nbsp;&nbsp;&nbsp;5.启动tomcat<br/>
&nbsp;&nbsp;&nbsp;&nbsp;6.输入地址：http://localhost:端口号/war包名字/ ，比如：http://localhost:8080/test/，<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果是ROOT.war，则不用输入war包名字，端口以tomcat中conf/server.xml配置的端口为准<br/>

