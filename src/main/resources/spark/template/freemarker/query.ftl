<#assign content>

	<h1>Providence Maps</h1>
	<div id="wrapper">
		<div id="query">
			<form method="POST" action="/path">
				Start Street 1 <input id="suggest" type="text" name="source1" rows="1" cols="85"></input> 
				<div class="suggestions">
					<select id="list"></select>
				</div>
				Start Street 2 <input id="suggest2" type="text" name="source2" rows="1" cols="85"></input> 
				<div class="suggestions">
					<select id="list2"></select>
				</div>
				<br>
				End Street 1 <input id="suggest3" type="text" name="target1" rows="1" cols="85"></input> 
				<div class="suggestions">
					<select id="list3"></select>
				</div>
				End Street 2 <input id="suggest4" type="text" name="target2" rows="1" cols="85"></input> 
				<div class="suggestions">
					<select id="list4"></select>
				</div>
				<br>
			  <input type="submit">
			</form>
		</div>
		<canvas id="map"></canvas>
	</div>
</#assign>
<#include "main.ftl">
