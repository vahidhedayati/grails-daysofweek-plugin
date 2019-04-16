<style>
#${instance.context} .weekDayContainer .btn-group .active { background-color : #5ADA5A; }
</style>
<g:set var="currentLocale" value="${instance?.locale?:
	session?.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'?:
	java.util.Locale.UK}"/>
<div id="${instance.context}">
	<g:if test="${instance?.showLabel}">
		<label for="dow" class="fieldLabel">
			<g:message code="weekDays.label" />
			<g:if test="${instance?.showLocale}">
				${currentLocale}
			</g:if>
		</label>
	</g:if>
	<div class="weekDayContainer">
		<div class="btn-group btn-xs" id="weekdaysElements" data-toggle="buttons-checkbox">
			<g:each var="day" status="i" in="${grails.utils.DaysOfWeek.daysByLocale(currentLocale)}">
	   			<label class="daysOfWeek btn btn-xs btn-${day.isWeekend?'warning':'default'} 
	   					${instance.daysOfWeekList?.contains(day.toString()) ? 'active' : ''}">
	   					<g:if test="${instance?.bindToBean}">
			       			<g:checkBox 
			       				data-text="${g.message(code:'dow.'+day)?:''}" class="hidden"
			                   	name="${instance?.fieldName?:'daysOfWeek'}[${i}]"  
			                   	value="${day}"  
			                   	checked="${instance.daysOfWeekList?.contains(day.toString())}"
			                   />
	                   </g:if>
	                   <g:else>
		                   <g:checkBox 
		       				data-text="${g.message(code:'dow.'+day)?:''}" class="hidden"
		                   	name="${instance?.fieldName?:'daysOfWeek'}"  
		                   	value="${day}"  
		                   	checked="${instance.daysOfWeekList?.contains(day.toString())}"
		                   />
	                   </g:else>
	       			<g:message code="dow.${day}"/>
	   			</label>
			</g:each>
		</div>
		<span class="btn btn-outline" id="toggleAll"><g:message code="all.label"/></span>
		<span class="btn btn-outline" id="toggleNone"><g:message code="none.label"/></span>
		<span class="btn btn-outline" id="toggleInvert"><g:message code="reverse.label"/></span>
	</div>
</div>
<script>
    $(function() {
        var context=document.getElementById('${instance.context}');
        $(".daysOfWeek",context).on("click",function() {
        	 $(this).find(':checkbox').prop('checked',!$(this).find(':checkbox').is(':checked'))
        });
        function toggleDays(mode) {
            $("#weekdaysElements :checkbox",context).each(function () {
                if ($(this).is(':checked') && mode=='none'||!$(this).is(':checked') && mode=='all'||mode=='invert') {
                   var p=$(this).parent();
                   p.button('toggle')
                   p.find(':checkbox').prop('checked', ! p.find(':checkbox').is(':checked'));
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