<#assign content>

	<h1>Providence Maps</h1>
	<div id="wrapper">
		<div id="query">
			<textarea id="suggest" name="source1" rows="1" cols="85">From Street 1</textarea>
			<select class="suggestions" id="list"></select>
			
			<textarea id="suggest2" name="source2" rows="1" cols="85">From Street 2</textarea>
			<select class="suggestions" id="list2"></select>
			
			<textarea id="suggest3" name="target1" rows="1" cols="85">To Street 1</textarea>
			<select class="suggestions" id="list3"></select>
						
			<textarea id="suggest4" name="target2" rows="1" cols="85">To Street 2</textarea>
			<select class="suggestions" id="list4"></select>
			<button type="button">Get Path</button>
		</div>
		<canvas id="map"></canvas>
	</div>
</#assign>
<#include "main.ftl">
