package org.gooru.navigatemap.processor.coursepath.flows;

public enum FlowVersion {

	V1_GLOBAL(1, "Global Flow with competency based Lesson and pre/post/BA/backfill - V1"),
	V2_NU(2, "Flow to suggest only resources after assessment and skipping logic based on competency completion - V2");

	private final int version;
	private final String versionInfo;

	FlowVersion(int versionArg, String versionInfoArg) {
		this.version = versionArg;
		this.versionInfo = versionInfoArg;
	}

	int flowVersion() {
		return this.version;
	}

	String flowVersionInfo() {
		return this.versionInfo;
	}
}
