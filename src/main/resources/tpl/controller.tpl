/**
 * Program  : ${modelName}Mapper.java
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ${rootPackage}.common.bean.Pagination;
import ${rootPackage}.common.exception.ServiceException;
import ${rootPackage}.common.web.controller.BaseController;
import ${rootPackage}.common.web.response.DataOut;
import ${rootPackage}.common.web.response.MessageOut;
import ${rootPackage}.common.web.response.Out;
 
 ${import}

/**
 * ${comment}
 * 
 * @author ${author}
 * @version 1.0.0
 * @date ${dateTime}
 */
@Controller("${modelVarName}Controller")
@RequestMapping(value = "/${modelVarName}", produces = "application/json;charset=UTF-8")
public class ${modelName}Controller extends BaseController<${modelName}> {

	@Resource(name = "${modelVarName}Service")
	private I${modelName}Service ${modelVarName}Service;

	/**
	 * 分页获取记录信息
	 * 
	 * @author ${author}
	 * @create ${dateTime}
	 * @param pagination
	 * @return Out
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Out<${modelName}> list(Pagination pagination) {
		try {
			return new DataOut<${modelName}>(this.${modelVarName}Service.loadOnePage(pagination), pagination);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取所有记录信息
	 * 
	 * @author ${author}
	 * @create ${dateTime}
	 * @return Out
	 */
	@RequestMapping("/listAll.do")
	@ResponseBody
	public Out<${modelName}> listAll() {
		try {
			return new DataOut<${modelName}>(this.${modelVarName}Service.loadAll(), null);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询
	 * 
	 * @author ${author}
	 * @create ${dateTime}
	 * @param ${modelVarName}
	 * @param pagination
	 * @return Out
	 */
	@RequestMapping("/search.do")
	@ResponseBody
	public Out<${modelName}> search(${modelName} ${modelVarName}, Pagination pagination) {
		try {
			return new DataOut<${modelName}>(this.${modelVarName}Service.search(${modelVarName}, pagination), pagination);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 新增记录
	 * 
	 * @author ${author}
	 * @create ${dateTime}
	 * @param ${modelVarName}
	 * @return Out<Object>
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public Out<Object> add(${modelName} ${modelVarName}) {
		try {
			this.${modelVarName}Service.save(${modelVarName});
			return MessageOut.ADD_OK_MESSAGE;
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MessageOut.ADD_FAIL_MESSAGE;
	}
	/**
	 * 删除
	 * 
	 * @author ${author}
	 * @create ${dateTime}
	 * @param id
	 * @return Out<Object>
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public Out<Object> delete(Long id) {
		try {
			this.${modelVarName}Service.deleteByKey(id);
			return MessageOut.DELETE_OK_MESSAGE;
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MessageOut.DELETE_FAIL_MESSAGE;
	}

	/**
	 * 修改记录信息
	 * 
	 * @author ${author}
	 * @create ${dateTime}
	 * @param ${modelVarName}
	 * @return Out<Object>
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public Out<Object> update(${modelName} ${modelVarName}) {
		try {
			this.${modelVarName}Service.update(${modelVarName});
			return MessageOut.UPDATE_OK_MESSAGE;
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MessageOut.UPDATE_FAIL_MESSAGE;
	}

	/**
	 * 根据ID获取记录
	 * 
	 * @author ${author}
	 * @create ${dateTime}
	 * @param id
	 * @return ${modelName}
	 */
	@RequestMapping("/getByKey.do")
	@ResponseBody
	public ${modelName} getByKey(Long id) {
		if (id != null && id > 0) {
			try {
				return this.${modelVarName}Service.getByKey(id);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}