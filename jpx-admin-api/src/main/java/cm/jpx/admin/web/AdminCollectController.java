package cm.jpx.admin.web;

import cm.jpx.admin.annotation.RequiresPermissionsDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import cm.jpx.core.util.ResponseUtil;
import cm.jpx.core.validator.Order;
import cm.jpx.core.validator.Sort;
import cm.jpx.db.domain.LitemallCollect;
import cm.jpx.db.service.LitemallCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/collect")
@Validated
public class AdminCollectController {
    private final Log logger = LogFactory.getLog(AdminCollectController.class);

    @Autowired
    private LitemallCollectService collectService;


    @RequiresPermissions("admin:collect:list")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户收藏"}, button = "查询")
    @GetMapping("/list")
    public Object list(String userId, String valueId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LitemallCollect> collectList = collectService.querySelective(userId, valueId, page, limit, sort, order);
        return ResponseUtil.okList(collectList);
    }
}
