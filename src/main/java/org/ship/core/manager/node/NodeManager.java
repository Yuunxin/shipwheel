package org.ship.core.manager.node;

import org.ship.core.dao.node.DnsDao;
import org.ship.core.dao.node.IpAddrDao;
import org.ship.core.dao.node.NicDao;
import org.ship.core.dao.node.RouteDao;
import org.ship.core.service.node.INodeService;
import org.ship.core.vo.node.Dns;
import org.ship.core.vo.node.IpAddress;
import org.ship.core.vo.node.Nic;
import org.ship.core.vo.node.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by wx on 2017/4/29.
 */
@Service
public class NodeManager implements INodeService {

    @Autowired
    private NicDao nicDao;

    @Autowired
    private IpAddrDao ipAddrDao;

    @Autowired
    private RouteDao routeDao;

    @Autowired
    private DnsDao dnsDao;

    @Override
    public Collection<Nic> getNicsByNodeId(int nodeId) {
        return nicDao.getNicsByNodeId(nodeId);
    }

    @Override
    public Nic getNic(int nicId) {
        return nicDao.getNic(nicId);
    }

    @Override
    public Collection<IpAddress> getIpAddrList(int nodeId) {
        return ipAddrDao.getIpAddrListByNodeId(nodeId);
    }

    @Override
    public Collection<IpAddress> getIpAddrList(int nodeId, int nicId) {
        return ipAddrDao.getIpAddrListByNodeIdAndNicId(nodeId, nicId);
    }

    @Override
    public IpAddress getIpAddr(int id) {
        return ipAddrDao.getIpAddr(id);
    }

    @Override
    public IpAddress createIpAddr(IpAddress ipAddress) {
        ipAddrDao.createIpAddr(ipAddress);
        return ipAddress;
    }

    @Override
    public IpAddress modIpAddr(IpAddress ipAddress) {
        ipAddrDao.modIpAddr(ipAddress);
        return  ipAddress;
    }

    @Override
    public void deleteIpAddr(int id) {
        ipAddrDao.deleteIpAddr(id);
    }

    @Override
    public Collection<Route> getRoutes(int nodeId) {
        return routeDao.getRoutes(nodeId);
    }

    @Override
    public Route getRoute(int id) {
        return routeDao.getRoute(id);
    }

    @Override
    public Route createRoute(Route route) {
        routeDao.createRoute(route);
        return route;
    }

    @Override
    public Route modRoute(Route route) {
        routeDao.modRoute(route.getGateway(), route.getId());
        return route;
    }

    @Override
    public void deleteRoute(int id) {
        routeDao.deleteRoute(id);
    }

    @Override
    public Dns getDns(int nodeId) {
        return dnsDao.getDns(nodeId);
    }

    @Override
    public Dns modDns(int nodeId, String dns) {
        dnsDao.modDns(nodeId, dns);
        Dns newDns = new Dns();
        newDns.setDns(dns);
        return newDns;
    }
}
