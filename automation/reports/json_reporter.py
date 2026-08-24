import os
import json
import datetime

class JSONReporter:
    @staticmethod
    def generate_json_report(results_data: list, output_dir: str):
        json_dir = os.path.join(output_dir, "JSON")
        os.makedirs(json_dir, exist_ok=True)

        total = len(results_data)
        passed = sum(1 for x in results_data if x["status"] == "PASSED")
        failed = sum(1 for x in results_data if x["status"] == "FAILED")
        pass_rate = (passed / total * 100) if total > 0 else 0

        payload = {
            "execution_info": {
                "timestamp": datetime.datetime.now().isoformat(),
                "total_tests": total,
                "passed": passed,
                "failed": failed,
                "pass_rate": f"{pass_rate:.1f}%"
            },
            "test_cases": results_data
        }

        filepath = os.path.join(json_dir, "execution-results.json")
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(payload, f, indent=2)

        print(f"JSON Report generated at: {filepath}")
