package com.pile.backend.controller;
import com.pile.backend.common.result.Result;
import com.pile.backend.pojo.dto.TicketRequestDTO;
import com.pile.backend.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "数据字典接口")
@RestController
@RequestMapping("/ticket")
@CrossOrigin
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("getTicketInfo")
    @ApiOperation(value = "获得查询的票的信息")
    public Result getTicketInfo(TicketRequestDTO ticketRequestDTO){
        return Result.ok(ticketRequestDTO);
    }


}
