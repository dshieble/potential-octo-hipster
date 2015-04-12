<#assign content>

	<div id="wrapper">
		<div id="query">
				<div class="target">
				<textarea id="suggest3" name="target1" rows="1" cols="50">To Street 1</textarea>
				<select class="suggestions" id="list3"></select>
							
				<textarea id="suggest4" name="target2" rows="1" cols="50">To Street 2</textarea>
				<select class="suggestions" id="list4"></select>
			</div>
			<div id="centerpiece">
				<h2>Providence Maps</h1>
				<button type="button">Get Path</button>
			</div>
			
			<div class="source">
				<textarea id="suggest" name="source1" rows="1" cols="50">From Street 1</textarea>
				<select class="suggestions" id="list" rows="1" cols="50"></select>
			
				<textarea id="suggest2" name="source2" rows="1" cols="50">From Street 2</textarea>
				<select class="suggestions" id="list2"></select>
			</div>

		</div>
		<canvas id="map"></canvas>
		<img id = "compass" src="http://www.clipartbest.com/cliparts/dT8/57M/dT857MjTe.png" alt="a compass">
	</div>
</#assign>
<#include "main.ftl">
