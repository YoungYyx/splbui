package com.splb.demo.controller;

import com.splb.demo.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    Service service;

    @RequestMapping(value = "/getRealEth0Speed",method = RequestMethod.POST)
    public String getRealEth0Speed(){
        return service.getRealEth0Speed();
    }

    @RequestMapping(value = "/getRealEth1Speed",method = RequestMethod.POST)
    public String getRealEth1Speed(){
        return service.getRealEth1Speed();
    }

    @RequestMapping(value = "/getRealEth0Rtt",method = RequestMethod.POST)
    public String getRealEth0Rtt(){
        return service.getRealEth0Rtt();
    }

    @RequestMapping(value = "/getRealEth1Rtt",method = RequestMethod.POST)
    public String getRealEth1Rtt(){
        return service.getRealEth1Rtt();
    }

    @RequestMapping(value = "/getSplbEth0Speed",method = RequestMethod.POST)
    public String getSPlBEth0Speed(){
        return service.getSplbEth0Speed();
    }

    @RequestMapping(value = "/getSplbEth1Speed",method = RequestMethod.POST)
    public String getSPlBEth1Speed(){
        return service.getSplbEth1Speed();
    }

    @RequestMapping(value = "/getMptcpSpeed",method = RequestMethod.POST)
    public String getMptcpSpeed(){
        return service.getMptcpSpeed();
    }

    @RequestMapping(value = "/splbStart",method = RequestMethod.POST)
    public void splbStart(){
        service.splbStart();
    }

    @RequestMapping(value = "/mptcpStart",method = RequestMethod.POST)
    public void mptcpStart(){
       service.mptcpStart();
    }

    @RequestMapping(value = "/splbEnd",method = RequestMethod.POST)
    public void splbEnd(){
        service.splbEnd();
    }

    @RequestMapping(value = "/mptcpEnd",method = RequestMethod.POST)
    public void mptcpEnd(){
        service.mptcpEnd();
    }

}
