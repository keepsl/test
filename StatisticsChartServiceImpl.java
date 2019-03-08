package com.newcapec.manager.service.impl;

import com.newcapec.core.service.AbstractService;
import com.newcapec.core.utils.DateUtils;
import com.newcapec.manager.dao.StatisticsChartDao;
import com.newcapec.manager.service.StatisticsChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/** 
 * <p>Title: StatisticsChartServiceImpl.java</p>  
 * <p>Description: 统计图形SERVICE接口实现类</p>  
 * <p>Copyright: Copyright (c) 新开普电子股份有限公司 2016</p>  
 * @author 李园园
 * @version
 * @date 创建日期：2018年6月29日
 * 修改日期：
 * 修改人：
 * 复审人：
 */
@Service
public class StatisticsChartServiceImpl extends AbstractService implements StatisticsChartService {
	
	@Autowired
	private StatisticsChartDao statisticsChartDao;
	
	@Override
	public List<Object[]> getData(Map<String, Object> params, String bh) {
		List<Object[]> listobj = statisticsChartDao.getData(params, bh);

		if ((listobj == null) || (listobj.isEmpty())) {
			return null;
		}
		//if (listobj.get(0)==null || listobj.get(0) instanceof Object[]) {
			//特殊处理《应用日访问量情况》数据
			if("app_access_time_chart".equals(bh)){
				Set<Integer> appidset = new HashSet<>();

				List<Object[]> listallobj = new ArrayList<>();

				for (Object[] obj:listobj){
					appidset.add(Integer.parseInt(obj[1].toString()));
				}
				for (Integer appid : appidset){
					List<Object[]> applistdata = new ArrayList<>();
						for (Object[] obj:listobj){
						if (appid.intValue()==Integer.parseInt(obj[1].toString())){
							applistdata.add(obj);
						}
					}
					String data = "";
					for (int i = 0;i<=24;i++){
						String val = "";
						if(i<applistdata.size()){
							val = applistdata.get(i)[3].toString();
						}else{
							val = "0";
						}
						data += val+",";
					}
					;
					Object[] obj = {applistdata.get(0)[0],data.substring(0,data.length()-1)};
					listallobj.add(obj);
				}
				return listallobj;
			}else if("server_online_chart".equals(bh)){
				List<Object[]> listallobj = new ArrayList<>();
				int ljnum = 0;
				int tjnum = 0;
				for (Object obj:listobj){
					if (obj==null){
						tjnum++;
						continue;
					}
					long s = (DateUtils.getNow().getTime() - DateUtils.parse(obj.toString(),"yyyy-MM-dd HH:mm:ss").getTime()) / (1000 * 60);
					if (s>=5){
						tjnum++;
					}else{
						ljnum++;
					}
				}

				Object[] obj = {"联机",ljnum};
				listallobj.add(obj);

				Object[] obj1 = {"脱机",tjnum};
				listallobj.add(obj1);

				return listallobj;

			}else{
				return listobj;
			}
		//}
		/*List l2 = new ArrayList(listobj.size());
	    for (Iterator localIterator2 = listobj.iterator(); localIterator2.hasNext(); ) {
	    	Object o = localIterator2.next();
	    	l2.add(new Object[] { o });
	    }
		return l2;*/
	}

}
