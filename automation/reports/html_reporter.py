import os
import datetime

class HTMLReporter:
    @staticmethod
    def generate_html_reports(results_data: list, output_dir: str):
        html_dir = os.path.join(output_dir, "HTML")
        os.makedirs(html_dir, exist_ok=True)

        total = len(results_data)
        passed = sum(1 for x in results_data if x["status"] == "PASSED")
        failed = sum(1 for x in results_data if x["status"] == "FAILED")
        skipped = sum(1 for x in results_data if x["status"] == "SKIPPED")
        pass_rate = (passed / total * 100) if total > 0 else 0

        # Build table rows HTML
        rows_html = ""
        for item in results_data:
            st = item["status"]
            badge_cls = "bg-success" if st == "PASSED" else ("bg-danger" if st == "FAILED" else "bg-warning")
            rows_html += f"""
            <tr>
                <td><code>{item['id']}</code></td>
                <td>{item['module']}</td>
                <td><strong>{item['name']}</strong></td>
                <td><span class="badge bg-secondary">{item['priority']}</span></td>
                <td><span class="badge {badge_cls}">{st}</span></td>
                <td>{item['duration']:.2f}s</td>
            </tr>
            """

        html_content = f"""<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VitalMatch Android Appium E2E Execution Report</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {{ background-color: #f8f9fa; font-family: 'Segoe UI', system-ui, sans-serif; }}
        .header-banner {{ background: linear-gradient(135deg, #1e293b, #ef4444); color: white; padding: 2.5rem 0; margin-bottom: 2rem; box-shadow: 0 4px 12px rgba(0,0,0,0.15); }}
        .card-stat {{ border: none; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); transition: transform 0.2s; }}
        .card-stat:hover {{ transform: translateY(-3px); }}
        .table-responsive {{ background: white; border-radius: 12px; padding: 1rem; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }}
    </style>
</head>
<body>
    <div class="header-banner">
        <div class="container">
            <h1 class="display-5 fw-bold">📱 VitalMatch Appium E2E Automation Report</h1>
            <p class="lead mb-0">Enterprise Android Mobile Testing Framework • Executed: {datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')}</p>
        </div>
    </div>

    <div class="container">
        <!-- Metrics Cards -->
        <div class="row g-3 mb-4">
            <div class="col-md-3">
                <div class="card card-stat bg-white p-3 text-center border-start border-primary border-4">
                    <div class="text-muted small fw-bold">TOTAL TEST CASES</div>
                    <div class="display-6 fw-bold text-dark">{total}</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card card-stat bg-white p-3 text-center border-start border-success border-4">
                    <div class="text-muted small fw-bold">PASSED</div>
                    <div class="display-6 fw-bold text-success">{passed}</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card card-stat bg-white p-3 text-center border-start border-danger border-4">
                    <div class="text-muted small fw-bold">FAILED</div>
                    <div class="display-6 fw-bold text-danger">{failed}</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card card-stat bg-white p-3 text-center border-start border-info border-4">
                    <div class="text-muted small fw-bold">PASS RATE</div>
                    <div class="display-6 fw-bold text-info">{pass_rate:.1f}%</div>
                </div>
            </div>
        </div>

        <!-- Detailed Results Table -->
        <div class="table-responsive">
            <h4 class="fw-bold mb-3">Execution Log</h4>
            <table class="table table-hover align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>Test ID</th>
                        <th>Module</th>
                        <th>Test Case Name</th>
                        <th>Priority</th>
                        <th>Status</th>
                        <th>Duration</th>
                    </tr>
                </thead>
                <tbody>
                    {rows_html}
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
"""

        with open(os.path.join(html_dir, "execution-report.html"), "w", encoding="utf-8") as f:
            f.write(html_content)

        with open(os.path.join(html_dir, "dashboard.html"), "w", encoding="utf-8") as f:
            f.write(html_content)

        with open(os.path.join(html_dir, "trends.html"), "w", encoding="utf-8") as f:
            f.write(html_content)

        print(f"HTML Reports generated successfully in: {html_dir}")
