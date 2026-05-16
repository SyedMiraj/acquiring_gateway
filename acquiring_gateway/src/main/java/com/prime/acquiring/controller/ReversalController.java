package com.prime.acquiring.controller;

import com.prime.acquiring.model.ReversalRequest;
import com.prime.acquiring.service.ReversalService;
import lombok.RequiredArgsConstructor;
import org.jpos.iso.ISOMsg;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reversal")
public class ReversalController {

    private final ReversalService reversalService;

    @PostMapping
    public String reverse(@RequestBody ReversalRequest request) throws Exception {
        ISOMsg response = reversalService.reverse(request);
        return response.getString(39);
    }
}