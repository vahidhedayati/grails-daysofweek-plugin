
<style>
#${instance.context} .weekDayContainer .btn-group .active { background-color : green }
#${instance.context}  .btn-group .active {
    color: #fff;
}
</style>
<div id="${instance.context}">

<g:set var="currenLocale" value="${instance?.locale?:
	session?.'org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE'?:
	java.util.Locale.UK}"/>
		
		
<label for="dow" class="fieldLabel">
	<g:message code="weekDays.label" />
</label>

<div class="weekDayContainer">
	<div class="btn-group btn-xs" id="weekdaysElements" data-toggle="buttons-checkbox">
		<g:each var="day" status="i" in="${grails.utils.DaysOfWeek.daysByLocale(currenLocale)}">
   			<label class="daysOfWeek btn btn-xs btn-${day.isWeekend?'warning':'danger'} 
   					${instance.daysOfWeekList?.contains(day.toString()) ? 'active' : ''}">
       			<g:checkBox 
       				data-text="${g.message(code:'dow.'+day)?:''}" class="hidden"
                   	name="${instance.fieldName ? instance.fieldName :'daysOfWeek'  }"  
                   	value="${day}"  
                   	checked="${instance.daysOfWeekList?.contains(day.toString())}"
                   />
       			<g:message code="dow.${day}"/>
   			</label>
		</g:each>
	</div>	

	<a class="btn btn-outline" id="toggleAll" href="#"><g:message code="all.label"/></a>
	<a class="btn btn-outline" id="toggleInvert" href="#"><g:message code="reverse.label"/></a>
</div>
</div>


<script>
    $(function() {
        var context=document.getElementById('${instance.context}');
        //a hack unsure why needed
        $(".daysOfWeek",context).on("click",function() {
        	 
        	 $(this).find(':checkbox').prop('checked',!$(this).find(':checkbox').is(':checked'))
             
        });
        function toggleDays(mode) {
            $("#weekdaysElements :checkbox",context).each(function () {
                if ($(this).is(':checked') && mode=='none') {
                    var p=$(this).parent();
                    p.button('toggle')
                    p.find(':checkbox').prop('checked', ! p.find(':checkbox').is(':checked'));
                } else if (!$(this).is(':checked') && mode=='all')  {
                    //$(this).parent().button('toggle').find(':checkbox').prop('checked');
                    var p=$(this).parent();
                    p.button('toggle')
                    p.find(':checkbox').prop('checked', ! p.find(':checkbox').is(':checked'));
                } else if (mode=='invert') {
                    //$(this).parent().button('toggle').find(':checkbox').prop('checked');
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
