<style>
#${instance.context} .weekDayContainer  .active { background-color : #5ADA5A; }
#${instance.context} .sideButtons { display:inline;}
#${instance.context} .hidden { display:none;}
#${instance.context} #weekdaysElements { display:inline;}
#${instance.context} .weekDayContainer .weekend { background-color : #f0ad4e; }
</style>
<g:set var="defaultLocale" value="${session?.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'}"/>
<g:set var="currentLocale" value="${instance?.locale?:
		defaultLocale?:
				java.util.Locale.UK}"/>
<div id="${instance.context}">
	<fieldset>
		<g:if test="${instance?.showLabel}">
			<label class="fieldLabel">
				<g:message code="weekDays.label" locale="${currentLocale}" />
				<g:if test="${instance?.showLocale}">
					${currentLocale}
				</g:if>
			</label>
		</g:if>
		<div class="weekDayContainer" >
			<div id="weekdaysElements">
				<g:each var="day" status="i" in="${grails.daysofweek.utils.DaysOfWeek.daysByLocale(currentLocale)}">
				<g:set var="containsItem" value="${instance.daysOfWeekList?.contains(day.toString())}"/>
		   			<label class="daysOfWeek ${!containsItem && day.isWeekend?'weekend':''}
		   					${containsItem ? 'active' : ''}">
		   					<g:if test="${instance?.bindToBean}">
				       			<g:checkBox
				       				data-text="${g.message(code:'dow.'+day)?:''}"
				       				data-weekend="${day.isWeekend}"
				       				class="hidden"
				                   	name="${instance?.fieldName?:'daysOfWeek'}[${i}]"
				                   	value="${day}"
				                   	checked="${instance.daysOfWeekList?.contains(day.toString())}"
				                   />
		                   </g:if>
		                   <g:else>
			                   <g:checkBox
			       				data-text="${g.message(code:'dow.'+day)?:''}" class="hidden"
			       				data-weekend="${day.isWeekend}"
			                   	name="${instance?.fieldName?:'daysOfWeek'}"
			                   	value="${day}"
			                   	checked="${instance.daysOfWeekList?.contains(day.toString())}"
			                   />
		                   </g:else>
							${day.longName}
		   			</label>
				</g:each>
			</div>
			<div class="sideButtons">
			<a class="ui-state-default ui-corner-all" id="toggleAll">
				<g:message code="all.label" locale="${currentLocale}"/></a>
			<a class="ui-state-default ui-corner-all" id="toggleNone">
				<g:message code="none.label" locale="${currentLocale}"/></a>
			<a class="ui-state-default ui-corner-all" id="toggleInvert">
				<g:message code="reverse.label" locale="${currentLocale}"/></a>
			</div>
		</div>
	</fieldset>
</div>
<script>
(function( $ ){
	//plugin buttonset vertical
	$.fn.buttonsetv = function() {
	  $(':radio', this).wrap('<div style="margin: 1px"/>');
	  $(':checkbox', this).wrap('<div style="margin: 1px"/>');
	  $(this).buttonset();
	 // $('label:first', this).removeClass('ui-corner-left').addClass('ui-corner-top');
	//  $('label:last', this).removeClass('ui-corner-right').addClass('ui-corner-bottom');
	};
	})( jQuery );
    $(function() {
        var context=document.getElementById('${instance.context}');
        $('#weekdaysElements',context).buttonsetv();
        $(".daysOfWeek",context).on("click",function() {
            var checked = $(this).find(':checkbox').is(':checked');
            if (!checked) {
            	$(this).removeClass('active').addClass('active').removeClass('weekend');
            } else {
            	$(this).removeClass('active');
                if($(this).find(':checkbox').data('weekend')) {
            		$(this).addClass('weekend');
                }
            }
        	$(this).find(':checkbox').prop('checked',!checked)
        });
        function toggleDays(mode) {
            $("#weekdaysElements :checkbox",context).each(function () {
                if ($(this).is(':checked') && mode=='none'||!$(this).is(':checked') && mode=='all'||mode=='invert') {
                    var checked = $(this).is(':checked');
                    if (!checked) {
                    	$(this).parent().parent().removeClass('active').addClass('active').removeClass('weekend');
                    } else {
                    	$(this).parent().parent().removeClass('active');
                    	if($(this).data('weekend')) {
                    		$(this).parent().parent().addClass('weekend');
                        }
                    }
                	$(this).prop('checked',!checked)
                }
            });
        }
        $('#toggleNone',context).on('click', function () {
            toggleDays('none')
        })
        $('#toggleInvert',context).on('click', function () {
            toggleDays('invert')
        })
        $('#toggleAll',context).on('click', function () {
            toggleDays('all')
        });
    });
</script>
