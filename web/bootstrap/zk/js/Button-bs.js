/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* global zul, zk */

zk.afterLoad('zul.wgt', function () {
    var _button = {};
 
zk.override(zul.wgt.Button.prototype, _button, {
    _sclass: 'btn-default',
    getZclass: function () {
        return 'btn';
    }
});
 
});

var calenderWidget; //ZK Calendar client side widget
zk.afterMount(function() {
    calenderWidget = zk.Widget.$(jq("$calendars")); // ZK id selector
    //Mouse wheel Handle - change month 
    jq('$calendars').bind(
        'mousewheel',
        function(event, delta) {
            if (calenderWidget.getMold() == "month") { //Only month mold need this 
                zAu.send(new zk.Event(calenderWidget, 'onMoveViewDate',delta > 0 ? 1 : -1));
                    return false;
            }
            return true;
        }
    );
});

