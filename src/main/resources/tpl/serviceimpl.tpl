/**
 * Program  : ${modelName}ServiceImpl.java
 * Author   : ${author}
 * Create   : ${dateTime}
 *
 * Copyright ${year} ${author}. All rights reserved.
 *
 * This software is the confidential and proprietary information of ${author}.  
 * You shall not disclose such Confidential Information and shall 
 * use it only in accordance with the terms of the license agreement 
 * you entered into with ${author}.
 *
 */
package ${package};
 
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import ${rootPackage}.common.data.IMapper;
import ${rootPackage}.common.service.impl.AbstractServiceImpl;
${import}

/**
 * ${comment}
 * 
 * @author ${author}
 * @version 1.0.0
 * @date ${dateTime}
 */
@Service("${modelVarName}Service")
public class ${modelName}ServiceImpl extends AbstractServiceImpl<${modelName}> implements I${modelName}Service {
	
	@Resource(name = "${modelVarName}Mapper")
	private ${modelName}Mapper ${modelVarName}Mapper;

	@Override
	public IMapper<${modelName}> getMapper() {
		return ${modelVarName}Mapper;
	}
}