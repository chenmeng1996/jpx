package cm.jpx.wx.service;

import cm.jpx.db.domain.LitemallRegion;
import cm.jpx.db.service.LitemallRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhy
 * @date 2019-01-17 23:07
 **/
@Component
public class GetRegionService {

	@Autowired
	private LitemallRegionService regionService;

	private static List<LitemallRegion> litemallRegions;

	protected List<LitemallRegion> getLitemallRegions() {
		if(litemallRegions==null){
			createRegion();
		}
		return litemallRegions;
	}

	private synchronized void createRegion(){
		if (litemallRegions == null) {
			litemallRegions = regionService.getAll();
		}
	}
}
